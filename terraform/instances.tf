resource "aws_key_pair" "keypair" {
    public_key = "${file("/home/felipe/Documentos/aws/keys/beerstore_key.pub")}"
}

resource "aws_instance" "instances" {
    count = 3

    ami = "ami-08a0d1e16fc3f61ea"
    instance_type = "t2.micro"

    subnet_id = "${element(aws_subnet.public_subnet.*.id, count.index)}"

    key_name = "${aws_key_pair.keypair.key_name}"

    vpc_security_group_ids = ["${aws_security_group.allow_ssh.id}"]

    tags = {
        Name = "hibicode_instances"
    }
}

output "public_ips" {
    value = "${join(", ", aws_instance.instances.*.public_ip)}"
}