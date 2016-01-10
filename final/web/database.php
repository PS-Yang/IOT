<?php
  $action=$_GET['action'];
  if($_GET['location']!=null)
    $location=$_GET['location'];
 
  //user information 
  $host = "140.120.13.77";
  $user = "huan123";
  $pass = "huan123";
  //database information
  $dbName = "lightdb";
  $table  = "light3";

  //Connect to mysql database
  $con = mysqli_connect($host,$user,$pass,$dbName);
  if (mysqli_connect_errno())//the reason connect fail 
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
  //設定成UTF8
  mysqli_set_charset($con,"UTF8");
  //Query command
  if($action==='location') 
  {
    $result = mysqli_query($con,"SELECT distinct location FROM $table");
  }
  else
  {
    $result = mysqli_query($con,"SELECT * FROM $table where location='$location'");
  }
  

  //store data to matrix
  $i=0; //MYSQLI_BOTH
  while ($row = mysqli_fetch_array($result))
  {
    $temp[$i]=$row;
    $i++;
  }

  //echo result as json 
  echo json_encode($temp);
  //var_dump(json_decode(json_encode($temp)));

  mysqli_close($con);
?>
