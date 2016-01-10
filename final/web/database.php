<?php
  $type=$_GET['type'];

  //user information 
  $host = "127.0.0.1";
  $user = "root";
  $pass = "123456";
  //database information
  $dbName  = "iot_final";
  $tbdata  = "final";
 // $tbuser  = "user";

  //Connect to mysql database
  $con = mysqli_connect($host,$user,$pass,$dbName);
  if (mysqli_connect_errno())//the reason connect fail 
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
  //設定成UTF8
  mysqli_set_charset($con,"UTF8");
  //Query command
  switch ($type) 
  {
    case '0':
      $result = mysqli_query($con,"SELECT distinct count FROM $tbdata");
      break;
    case '1':
      $count=$_GET['count'];
      if($count==3)
        $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count  LIMIT 1000");
       // $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count and shock>0");
      else
        $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count");
      break;
    default:
      //$result = mysqli_query($con,"SELECT * FROM $table where location='$location'");
      break;
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
  mysqli_close($con);
?>
