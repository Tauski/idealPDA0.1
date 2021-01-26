<?php
$mysqli = new mysqli("localhost","root","","firstdb");

// Check connection
if ($mysqli -> connect_errno) {
  echo "Failed to connect to MySQL: " . $mysqli -> connect_error;
  exit();
}
else{
	echo "connection successful"; 
}
?>