<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        
        if(!($this->connect)){
            die("Error in connection: " . mysqli_connect_errno());
        }

        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where c_username = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['c_username'];
            $dbpassword = $row['c_password'];
            if ($dbusername == $username && $password == $dbpassword) { // password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;
        return $login;
    }

    function signUp($table, $firstname, $lastname, $username, $password, $email)
    {
        $firstname = $this->prepareData($firstname);
        $lastname = $this->prepareData($lastname);
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $email = $this->prepareData($email);
        //$password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (c_firstname, c_lastname, c_username, c_password, c_email) VALUES ('" . $firstname . "','" . $lastname . "','" . $username . "','" . $password . "','" . $email . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function syncinfo($table, $barcode_id, $containerName, $latitude, $longitude, $row, $section, $lastUpdated, $syncStatus)
    {
        $barcode_id = $this->prepareData($barcode_id);
        $containerName = $this->prepareData($containerName);
        $latitude = $this->prepareData($latitude);
        $longitude = $this->prepareData($longitude);
        $row = $this->prepareData($row);
        $section = $this->prepareData($section);
        $lastUpdated = $this->prepareData($lastUpdated);
        $syncStatus = 0;
        $this->sql =
            "INSERT INTO " . $table . " (b_barcodeId, b_containerName, b_latitude, b_longitude, b_row, b_section, b_lastUpdated, b_syncStatus) VALUES ('" . $barcode_id . "','" . $containerName . "','" . $latitude . "','" . $longitude . "','" . $row . "','" . $section . "','" . $lastUpdated . "','" . $syncStatus . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
}

?>
