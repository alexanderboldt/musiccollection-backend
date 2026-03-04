locals {
  database = {
    name = "musiccollection"
    username = "admin"
    password = "adminadmin"
    port = 5432
  }

  s3 = {
    region = "eu-central-1"
  }

  musiccollection_app = {
    name = "musiccollection-app"
    image = "ghcr.io/alexanderboldt/musiccollection:8.0.0"
    port = 4000
  }
}
