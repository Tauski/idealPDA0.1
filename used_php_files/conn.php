<?php
$db_name="firstdb"; // database name
$mysql_username="root";
$mysql_password="";
$server_name="localhost";
$port="3306";
$conn = mysqli_connect($server_name,$mysql_username,$mysql_password,$db_name,$port);
if($conn)
{
  //echo "Connection Success";
}
else
{
  //echo "Connection Failed";
}
?>