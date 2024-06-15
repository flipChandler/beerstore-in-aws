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

    ingress {
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
