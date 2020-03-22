<?php

include 'db_connection.php';


if($_SERVER['REQUEST_METHOD']=='POST'){

	$name = $_POST['name']; 
    $address = $_POST['address'];
	$password = $_POST['password'];
	$email = $_POST['email'];
    $phone = $_POST['phone'];
  

}

if($_SERVER['REQUEST_METHOD']=='GET'){

	$name = $_GET['name']; 
    $address = $_GET['address'];
	$password = $_GET['password'];
	$email = $_GET['email'];
    $phone = $_GET['phone'];
}


	
 
 $sql = "INSERT INTO user_data ( name,password,address,email,phone)VALUES
 									('$name','$password','$address','$email','$phone')";

 $result=mysqli_query($conn,$sql);


//if($result)
//    {
    

//    $response = array("response"=>"success");
//      echo json_encode($response);
//    }
// else
//    {
//      $response = array("response"=>"failure");
//      echo json_encode($response);
//    }

$sql_select="SELECT * FROM `user_data` WHERE `email`='$email' && `password`= '$password' ";
$result_select=mysqli_query($conn,$sql_select);

$result_count=mysqli_num_rows($result_select);

if($result_count>0)
{
    while($row=mysqli_fetch_array($result_select))
        {
            $name=$row['name'];
            $phone=$row['phone'];
            $address=$row['address'];
            $password=$row['password']; 
            $email=$row['email']; 
             $user_id=$row['user_id'];
           
  

             $UserDetails=array(
                "user_id" =>$user_id,
                                "name" =>$name,
                                "phone"=>$phone,
                                "address" => $address,
                                "password" => $password,
                                "email" => $email
                                

                                );
     

        }

   
   
}

    if($result_count)
    {          
       $response=array("response"=> $UserDetails);
       echo json_encode($response);
       
    }else
    {
        $response=array("response"=> "failure");
        echo json_encode($response);
    }
?>