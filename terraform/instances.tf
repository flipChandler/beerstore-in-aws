resource "aws_instance" "instances" {
    count = 3

    ami = "ami-08a0d1e16fc3f61ea"
    instance_type = "t2.micro"

    subnet_id = "${element(aws_subnet.public_subnet.*.id, count.index)}"

    tags = {
        Name = "hibicode_instances"
    }
}