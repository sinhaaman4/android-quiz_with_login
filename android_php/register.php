<?php
	include 'db.php';
	$user_id=$_REQUEST['user_id'];
	$password=$_REQUEST['password'];
	$password=md5($password);
	$security_question=$_REQUEST['security_question'];
	$answer=$_REQUEST['answer'];
	$score = 0;
	mysqli_query($link,"insert into user_master values('$user_id','$password','$security_question','$answer','$score')");
	
	echo "registered successfully";
?>