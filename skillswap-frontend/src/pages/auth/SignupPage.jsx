import { useState } from "react";

import {
useNavigate,
Link
}
from "react-router-dom";

import {
useForm
}
from "react-hook-form";

import toast
from "react-hot-toast";

import Input
from "../../components/ui/Input";

import Button
from "../../components/ui/Button";

import {
signupApi
}
from "../../api/authApi";

const SignupPage=()=>{

const navigate=
useNavigate();

const [loading,setLoading]=
useState(false);

const{

register,
handleSubmit,
formState:{errors}

}=useForm();


const onSubmit=
async(data)=>{

try{

setLoading(true);

await signupApi(
data
);

toast.success(
"Signup successful"
);

navigate(
"/login"
);

}

catch(error){

toast.error(

error.response?.data?.message ||

"Signup failed"

);

}

finally{

setLoading(false);

}

};

return(

<div className="auth-container">

<div className="auth-card">

<h1>

Create Account

</h1>

<form
onSubmit={
handleSubmit(
onSubmit
)
}
>

<Input
label="Name"

{
...register(
"name",
{
required:
"Name required"
}
)
}

error={
errors.name?.message
}
/>

<Input
label="Email"

{
...register(
"email",
{
required:
"Email required"
}
)
}

error={
errors.email?.message
}
/>

<Input
label="Password"
type="password"

{
...register(
"password",
{
required:
"Password required"
}
)
}

error={
errors.password?.message
}
/>

<Button
type="submit"
loading={loading}
>

Signup

</Button>

</form>

<p>

Already have account?

<Link to="/login">

 Login

</Link>

</p>

</div>

</div>

);

};

export default SignupPage;