terraform {
    backend "s3" {
      bucket = "flip-chandler-terraform-state"
      key = "beerstore-in-aws"
      region = "us-east-1"
      profile = "terraform"
    }    
}