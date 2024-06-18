data "aws_secretsmanager_secret_version" "creds" {
  secret_id = "db-creds-hibicode"
}

locals {
  db_creds = jsondecode(
    data.aws_secretsmanager_secret_version.creds.secret_string
  )
}

resource "aws_db_instance" "rds" {  
  identifier = "hibicode-beerstore-rds" 
  engine = "postgres"
  engine_version = "16.3"
  instance_class = "db.t3.micro"
  allocated_storage = "100"

  db_name = "beerstoredb"
  username = local.db_creds.username
  password = local.db_creds.password
  port = "5432"

  db_subnet_group_name    = "${aws_db_subnet_group.rds_subnet_group.name}"
  vpc_security_group_ids = ["${aws_security_group.database.id}"]

  maintenance_window = "Thu:03:30-Thu:05:30"
  backup_window = "05:30-06:30"
  storage_type = "gp2"
  multi_az = "false"
  skip_final_snapshot = true
}

resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "tf-rds-${var.subnet_group_name}"
  subnet_ids = "${flatten(chunklist(aws_subnet.private_subnet.*.id, 1))}"

  tags = {
    Name = "tf-rds-${var.subnet_group_name}"
  }
}

resource "aws_db_parameter_group" "custom_parameter_group" {
  name   = "custom-rds-pg"
  family = "postgres16"

  parameter {
    name  = "rds.force_ssl"
    value = "0"
  }
}

output "db_instance_endpoint" {
  description = "The connection endpoint"
  value       = aws_db_instance.rds.endpoint
}

output "db_instance_name" {
  description = "The database name"
  value       = aws_db_instance.rds.db_name
}

output "db_instance_username" {
  description = "The master username for the database"
  value       = aws_db_instance.rds.username
  sensitive   = true
}

output "db_subnet_group_name" {
  description = "The db subnet group name"
  value       = aws_db_instance.rds.db_subnet_group_name
}