<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
include 'DatabaseConfig.php';

$con = mysqli_connect($HostName,$User,$Password,$dbName,$Port);

$FL_NAME = $_POST['fl_name'];
$EVENT = $_POST['event'];
$TIMEOFEVENT = $_POST['etime'];
$LOCATION = $_POST['elocation'];
$DESCRIPTION = $_POST['edescription'];

	$sql_query = "INSERT INTO userevents (user,event,timeofevent,location,description) values ('$FL_NAME','$EVENT','$TIMEOFEVENT','$LOCATION','$DESCRIPTION')";
	if(mysqli_query($con,$sql_query)){
		echo 'Event saved!';
	}
	else{
		echo 'something else went wrong';
	}
}
mysqli_close($con);
?>