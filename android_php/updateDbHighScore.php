<?php
	include 'db.php';
		$user_id=$_REQUEST['user_id'];
		$newScore=$_REQUEST['newScore'];
		$result=mysqli_query($link,"UPDATE user_master SET highscore='$newScore' WHERE user_id='$user_id'");
			
?> 