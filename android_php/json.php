<?php
	include 'dp.php';
	$result=mysqli_query($link,"select * from user_master");
	$arr=mysqli_fetch_all($result,MYSQLI_ASSOC);
	echo json_encode($arr);
?>