<?php
require "DataBase.php";
$db = new DataBase();
$response = array();
if (isset($_POST['c_username']) && isset($_POST['c_password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("credentials", $_POST['c_username'], $_POST['c_password'])) {
            //$response["success"] = "Login Success";
            echo "Login Success";
        } else {
            //$response["success"] = "Username or Password wrong";
            echo "Username or Password wrong";
        }
    } else { 
       // $response["success"] = "Error: Database connection";
        echo "Error: Database connection";
    }
} else {
    //$response["success"] = "All fields are required";
    echo "All fields are required";
}
//echo json_encode($response);
?>
