<?php

    $host = "127.0.0.1";
    $user = "user";//user
    $pass = "pass";//password

    //資料庫資訊
    $databaseName = "lightdb";
    $tableName = "light";


    //連結資料庫
    $con = mysql_connect($host,$user,$pass);
    $dbs = mysql_select_db($databaseName, $con);


    for($i=0;$i<100;$i++) {
		$sql = "INSERT INTO $tableName (value,status) VALUES (".rand(0,1023).",".rand(0,1).")";
		mysql_query($sql);
    }
?>