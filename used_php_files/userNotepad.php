<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
include 'DatabaseConfig.php';

$con = mysqli_connect($HostName,$User,$Password,$dbName,$Port);

$FL_NAME = $_POST['fl_name'];
$NOTE = $_POST['savednote'];

	$sql_query = "INSERT INTO usernotes (user,note) values ('$FL_NAME','$NOTE')";
	if(mysqli_query($con,$sql_query)){
		echo 'Note saved';
	}
	else{
		echo 'something else went wrong';
	}
}
mysqli_close($con);
?>