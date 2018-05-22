<?php
define('DB_NAME', 'college');
define('DB_HOST','localhost');
define('DB_USER','root');
define('DB_PASS','');

$link = mysqli_connect(DB_HOST,DB_USER,DB_PASS);
$db_selected = mysqli_select_db($link,DB_NAME);

if(!$link){
	die('cant connect'.mysqli_error());
}

?>