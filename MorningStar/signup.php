<?php
require "DataBase.php";
$db = new DataBase();
$response = array();
if (isset($_POST['c_firstname']) && isset($_POST['c_lastname']) && isset($_POST['c_username']) && isset($_POST['c_password']) && isset($_POST['c_email'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("credentials", $_POST['c_firstname'], $_POST['c_lastname'], $_POST['c_username'], $_POST['c_password'], $_POST['c_email'])) {
            //$response["success"] = "Registration Successful";
            echo "Registration Successful";
        } else //$response["success"] = "Sign up Failed";
        echo "Sign up Failed";
    } else //$response["success"] = "Error: Database connection"; 
    echo "Error: Database connection";
} else //$reponse["success"] = "All fields are required";
echo "All fields are required";
//echo json_encode($response);
?>
