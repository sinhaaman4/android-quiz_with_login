<?php
	include 'db.php';
		$user_id=$_REQUEST['user_id'];
		$password=$_REQUEST['password'];
		$password=md5($password);
		$result=mysqli_query($link,"SELECT * FROM user_master WHERE user_id='$user_id' AND password='$password'");

			if($arr=mysqli_fetch_assoc($result))
			{
				echo "success";
			}
			else
			{
				echo "invalid login details";
			}
?>