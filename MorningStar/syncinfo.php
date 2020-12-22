<?php
require "DataBase.php";
$db = new DataBase();
$response = array();
if (isset($_POST['b_barcodeId']) && isset($_POST['b_containerName']) && isset($_POST['b_latitude']) && isset($_POST['b_longitude']) && isset($_POST['b_row']) && isset($_POST['b_section']) && isset($_POST['b_lastUpdated']) && isset($_POST['b_syncStatus'])) {
    if ($db->dbConnect()) {
        if ($db->syncinfo("barcodes", $_POST['b_barcodeId'], $_POST['b_containerName'], $_POST['b_latitude'], $_POST['b_longitude'], $_POST['b_row'], $_POST['b_section'], $_POST['b_lastUpdated'], $_POST['b_syncStatus'])) {
            $response["success"] = "Sync Successful";
            // echo "Sync Successful";
        } else $response["success"] = "Sync Failed";
        // echo "Sync Failed";
    } else $response["success"] = "Error: Database connection";
    // echo "Error: Database connection";
} else $response["success"] = "Error while Synching";
// echo "Error While Synching";
echo json_encode($response);
?>