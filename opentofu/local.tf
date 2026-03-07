locals {
  database = {
    name = "musiccollection"
    username = "adminadmin"
    password = "adminadmin"
    port = 5432
  }

  s3 = {
    region = "eu-central-1"
  }

  musiccollection_app = {
    name = "musiccollection-app"
    image = "ghcr.io/alexanderboldt/musiccollection:no-auth"
    port = 4000
  }
}
