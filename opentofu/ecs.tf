resource "aws_ecs_cluster" "app_cluster" {
  name = "musiccollection-app-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "musiccollection-app-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"

  task_role_arn            = aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = local.musiccollection_app.name
      image     = local.musiccollection_app.image
      essential = true
      portMappings = [
        {
          containerPort = local.musiccollection_app.port
          hostPort      = local.musiccollection_app.port
        }
      ]
      environment = [
        { name = "POSTGRES_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.endpoint}/${local.database.name}" },
        { name = "POSTGRES_USER", value = local.database.username },
        { name = "POSTGRES_PASSWORD", value = local.database.password },
        { name = "AUTH_SERVER_URL", value = "https://cognito-idp.${local.s3.region}.amazonaws.com/${aws_cognito_user_pool.main.id}" },
        { name = "AUTH_CLIENT_ID", value = aws_cognito_user_pool_client.client.id },
        { name = "AUTH_CLIENT_SECRET", value = aws_cognito_user_pool_client.client.client_secret },
        { name = "S3_REGION", value = local.s3.region },
        { name = "BUCKET_ARTIST", value = aws_s3_bucket.musiccollection_bucket.bucket },
        { name = "BUCKET_ALBUM", value = aws_s3_bucket.musiccollection_bucket.bucket }
      ]
    }
  ])
}

resource "aws_ecs_service" "app_service" {
  name            = "musiccollection-app-service"
  cluster         = aws_ecs_cluster.app_cluster.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.public_a.id, aws_subnet.public_b.id]
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = true   # <-- important for GHCR image pull
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "ecs-sg"
  description = "ECS tasks security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = local.musiccollection_app.port
    to_port     = local.musiccollection_app.port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "ecs_task_role" {
  name = "musiccollection-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}
