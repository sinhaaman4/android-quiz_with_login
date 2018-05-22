<?php
	include 'db.php';
		$user_id=$_REQUEST['user_id'];
		$result=mysqli_query($link,"SELECT highscore FROM user_master WHERE user_id='$user_id'");

			if($arr=mysqli_fetch_assoc($result)){
				
				echo $arr["highscore"];
			}
			
?> 