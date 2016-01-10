<?php
  $type=$_GET['type'];

  //user information 
  $host = "127.0.0.1";
  $user = "user";
  $pass = "pass";
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
      $i=0; //MYSQLI_BOTH
     //store data to matrix
      while ($row = mysqli_fetch_array($result))
      {
        $temp[$i]=$row;
        $i++;
      }
      break;
    case '1':

      $count=$_GET['count'];
      if($count==3)
        $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count  LIMIT 1000");
       // $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count and shock>0");
      else
        $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count");
      //store data to matrix
      $i=0; //MYSQLI_BOTH
      while ($row = mysqli_fetch_array($result))
      {
        $temp[$i]=$row;
        $i++;
      }
      break;
    case '2':
      $result = mysqli_query($con,"SELECT distinct count FROM $tbdata");
      $i=0;
      while ($row = mysqli_fetch_array($result))
      {
        $count=$row['count'];
        //echo $count+" ";
        $result2 = mysqli_query($con,"SELECT * FROM $tbdata where count=$count and shock>0");
        $allcount=0;
        while ($row2 = mysqli_fetch_array($result2))
        {
          $allcount++;
        }
        $temp[$i][0]=$count;
        $temp[$i][1]=$allcount;
        $i++;
      }
      break;
      case '3':
      $count=$_GET['count'];
      $result = mysqli_query($con,"SELECT * FROM $tbdata where count=$count and shock>0");
      $i=0; //MYSQLI_BOTH
      while ($row = mysqli_fetch_array($result))
      {
        $i++;
      }
      $temp=$i;
      break;
    default:
      //$result = mysqli_query($con,"SELECT * FROM $table where location='$location'");
      break;
  }
 
  //echo result as json 
  echo json_encode($temp);
  mysqli_close($con);
?>
