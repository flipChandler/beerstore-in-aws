module "rds" {
  source  = "terraform-aws-modules/rds/aws"
  version = "6.7.0"

  identifier = "hibicode-beerstore-rds"

  engine = "postgres"
  engine_version = "16.3"
  instance_class = "db.t3.micro"
  allocated_storage = "100"

  db_name = "beerstoredb"
  username = "felipeadmin"
  password = "root@1234"
  port = "5432"

  db_subnet_group_name    = "${aws_db_subnet_group.rds_subnet_group.name}"
  vpc_security_group_ids = ["${aws_security_group.database.id}"]

  maintenance_window = "Thu:03:30-Thu:05:30"
  backup_window = "05:30-06:30"
  storage_type = "gp2"
  multi_az = "false"
  family = "postgres16"

  # subnet_ids = "${flatten(chunklist(aws_subnet.private_subnet.*.id, 1))}"
}

resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "tf-rds-${var.cluster_name}"
  subnet_ids = "${flatten(chunklist(aws_subnet.private_subnet.*.id, 1))}"

  tags = {
    Name = "tf-rds-${var.cluster_name}"
  }
}

