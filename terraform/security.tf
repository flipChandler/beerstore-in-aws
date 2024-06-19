# security group - ec2 instances via ssh for only this IP
resource "aws_security_group" "allow_ssh" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_allow_ssh"

    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["${var.my_public_ip}"]
    }
}

# security group - database
resource "aws_security_group" "database" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_database"
    description = "enable postgres rds access on port 5432"

    ingress {
        description = "postgres rds access"
        from_port = 5432
        to_port = 5432
        protocol = "tcp"
        self = true
    }
}


# ansible will be able to access internet to pull docker image to install in the ec2 instances
resource "aws_security_group" "allow_outbound" {
  vpc_id = "${aws_vpc.main.id}"
  name = "hibicode_allow_outbound"

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# managers of Docker Swarm can have comunication among them
resource "aws_security_group" "cluster_comunication" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_cluster_comunication"

    # whoever (ec2 instances) has this security group, will receive services in the 2377
    ingress {
        from_port = 2377
        to_port = 2377
        protocol = "tcp"
        self = true
    }

    ingress {
        from_port = 7946
        to_port = 7946
        protocol = "tcp"
        self = true
    }

    ingress {
        from_port = 7946
        to_port = 7946
        protocol = "udp"
        self = true
    }

     ingress {
        from_port = 4789
        to_port = 4789
        protocol = "udp"
        self = true
    }
}

resource "aws_security_group" "allow_app" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_allow_app"

    // access portainer interface only from my IP
    ingress {
        from_port = 9000
        to_port = 9000
        protocol = "tcp"
        cidr_blocks = ["${var.my_public_ip}"]
    }

    ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = [
      for subnet in aws_subnet.public_subnet : subnet.cidr_block
    ]
  }
}

resource "aws_security_group" "allow_load_balancer" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_allow_load_balancer"

    //all IP's are allowed to make request
    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    // requests go out from this lb only for ec2 instances in the port 8080 (spring application)
    egress {
        from_port = 8080
        to_port = 8080
        protocol = "tcp"
        cidr_blocks = [
            for subnet in aws_subnet.public_subnet : subnet.cidr_block
        ]
    }
}
