# security group - ec2 instances via ssh for only this IP
resource "aws_security_group" "allow_ssh" {
    vpc_id = "${aws_vpc.main.id}"
    name = "hibicode_allow_ssh"

    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["45.175.104.157/32"]
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
