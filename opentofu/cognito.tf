resource "aws_cognito_user_pool" "main" {
  name = "musiccollection-user-pool"

  auto_verified_attributes = ["email"]

  password_policy {
    minimum_length = 8
  }
}

resource "aws_cognito_user_pool_client" "client" {
  name            = "musiccollection-client"
  user_pool_id    = aws_cognito_user_pool.main.id
  generate_secret = false

  explicit_auth_flows = [
    "ALLOW_USER_AUTH",
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_ADMIN_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH"
  ]
}
