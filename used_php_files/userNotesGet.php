<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include 'DatabaseConfig.php';
 
 $con = mysqli_connect($HostName,$User,$Password,$dbName,$Port);
 
 $FL_NAME = $_POST['f_name'];
 
 $Sql_Query = "SELECT note FROM usernotes WHERE user = '$FL_NAME' AND note IS NOT NULL";
 
$result = array();
$result['data'] = array();
$response = mysqli_query($con,$Sql_Query);

#incomplete as of yet, it only echoes the first element

while($row = mysqli_fetch_array($response))
{
		$index['note'] = $row['0'];
		array_push($result['data'], $index);
}

$result["success"] = "1";
echo json_encode($result);

}
mysqli_close($con);
?>