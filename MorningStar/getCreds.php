<?php
$host = 'localhost';
$user = 'root';
$pwd = '';
$db = 'morningstar';

$conn = mysqli_connect($host, $user, $pwd, $db);

if(!$conn) {
    die("Error in connection: " . $conn->connect_error);
}

$response = array();
$sql_query = "SELECT * FROM credentials";
$result = mysqli_query($conn, $sql_query);

if(mysqli_num_rows($result) > 0) {
    $response['success'] = 1;
    $users = array();
    while($row = mysqli_fetch_assoc($result)) {
        array_push($users, $row);
    }
    $response['users'] = $users;
}else {
    $response['success'] = 0;
    $resoponse['message'] = 'No data';
}
echo json_encode($response);
mysqli_close($conn);
?>
