variable "availability_zones" {
    default = [
        "us-east-1a",
        "us-east-1b",
        "us-east-1c"
    ]
}


variable "subnet_group_name" {
    default = "hibicode_subnet_group"
}

variable "my_public_ip" {}