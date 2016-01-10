<?php
  
  $value = $_GET['value'];

  //user information
  $host = "127.0.0.1";
  $user = "user";
  $pass = "pass";

  //database information
  $databaseName = "iot_final";
  $tableName = "final";

  //Connect to mysql database
  $con = mysqli_connect($host,$user,$pass,$databaseName);
  if (mysqli_connect_errno())//the reason connect fail 
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
  //設定成UTF8
  mysqli_set_charset($con,"UTF8");
  //Query database for data
  $result = mysqli_query($con,"INSERT into $tableName (sound,shock,light,type,count) VALUES ($value)");
  //store matrix
  if($result==1)
    echo "success";
  else
    echo "error";
?>