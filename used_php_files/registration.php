<?php
    include('conn.php');
    if($_SERVER['REQUEST_METHOD'] == 'POST')
  {    
   
  $fname=$_POST['name']; //data recieved from android app
  $city=$_POST['city'];
  $phone=$_POST['phone'];
  
  $query="select * from registration where phone='$phone'";
  
        $result=mysqli_query($conn,$query);
    if(mysqli_num_rows($result)>0)
    {  
           while($row=mysqli_fetch_array($result))
          {
            echo trim("false");
          }
    }
        else
        {
          $query="insert into registration (name,city,phone) values ('$fname','$city','$phone');";
          mysqli_query($conn,$query);
          echo trim("true");
        }
  }   
?> 