resource "aws_cognito_user_pool" "main" {
  name = "musiccollection-user-pool"

  auto_verified_attributes = ["email"]
}

resource "aws_cognito_user_pool_client" "client" {
  name            = "musiccollection-client"
  user_pool_id    = aws_cognito_user_pool.main.id
  generate_secret = true

  allowed_oauth_flows                   = ["code"]
  allowed_oauth_scopes                  = ["openid", "profile", "email"]
  supported_identity_providers          = ["COGNITO"]
  allowed_oauth_flows_user_pool_client  = true
  callback_urls                         = ["https://d84l1y8p4kdic.cloudfront.net"]

  explicit_auth_flows = [
    "ALLOW_USER_AUTH",
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH"
  ]
}

resource "aws_cognito_user_pool_domain" "domain" {
  domain       = "musiccollection-auth-domain"
  user_pool_id = aws_cognito_user_pool.main.id
}

output "cognito_client_id" {
  value = aws_cognito_user_pool_client.client.id
}

output "cognito_client_secret" {
  value     = aws_cognito_user_pool_client.client.client_secret
  sensitive = true
}
