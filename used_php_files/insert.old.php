<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = "localhost";
$username = "root";
$password = "";
$dbname = "firstdb";
$port = 3306;


//$mysqli = new mysqli($host,$username,$password,$dbname,$port);
$con=mysqli_connect($host,$username,$password,$dbname,$port);
// Check connection
if ($con -> connect_errno) {
  echo "Failed to connect to MySQL: " . $mysqli -> connect_error;
  exit();
}

$data= $_POST['data'];
$sql= "INSERT INTO users(name) VALUES('$data')";
if(mysqli_query($con,$sql)){
	echo "success insert";
}
else{
	echo "failed insert: " ;
}

mysqli_close($con);
?>