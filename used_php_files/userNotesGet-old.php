<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include 'DatabaseConfig.php';
 
 $con = mysqli_connect($HostName,$User,$Password,$dbName,$Port);
 
 $FL_NAME = $_POST['f_name'];
 
 $Sql_Query = "SELECT note FROM usernotes WHERE user = '$FL_NAME' AND note IS NOT NULL";
 
 $result = mysqli_query($con,$Sql_Query);

#incomplete as of yet, it only echoes the first element

if( mysqli_num_rows($result) > 0 ){

  $mbno_arr = array();

    while ($row = mysqli_fetch_array($result))
        $mbno_arr[] = $row[0];

    if( count($mbno_arr) > 0 ){    	
        echo implode(',',array_filter($mbno_arr));
} 
   else{
        echo 'No number is there';
}
}
}
mysqli_free_result($result);
?>