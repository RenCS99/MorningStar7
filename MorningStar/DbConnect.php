<?php
$host = 'localhost';
$user = 'root';
$pwd = '';
$db = 'morningstar';

$conn = mysqli_connect($host, $user, $pwd, $db);

if(mysqli_connect_errno()){
    echo "Fail to connect with database".mysqli_connect_err();
}

echo "Connected successfully";
?>