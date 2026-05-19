import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";

import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

import { loginApi } from "../../api/authApi";
import useAuth from "../../hooks/useAuth";

const LoginPage = () => {

    const navigate = useNavigate();

    const { login } = useAuth();

    const [loading,setLoading] =
    useState(false);

    const {

        register,
        handleSubmit,
        formState:{errors}

    } = useForm();


    const onSubmit = async(data)=>{

        try{

            setLoading(true);

            const response =
            await loginApi(data);

            console.log(
                response.data
            );

            const token =
            response.data.token;

            const user =
            response.data.user || {
                email:data.email
            };

            login(
                token,
                user
            );

            toast.success(
                "Login successful"
            );

            navigate(
                "/dashboard",
                {
                    replace:true
                }
            );

        }

        catch(error){

            toast.error(

                error.response?.data?.message ||

                "Login failed"

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
                    Welcome Back
                </h1>

                <form
                onSubmit={
                    handleSubmit(
                        onSubmit
                    )
                }
                >

                    <Input
                    label="Email"
                    type="email"

                    {...register(
                        "email",
                        {
                            required:
                            "Email required"
                        }
                    )}

                    error={
                        errors.email?.message
                    }
                    />

                    <Input
                    label="Password"
                    type="password"

                    {...register(
                        "password",
                        {
                            required:
                            "Password required"
                        }
                    )}

                    error={
                        errors.password?.message
                    }
                    />

                    <Button
                    type="submit"
                    loading={loading}
                    >

                        Login

                    </Button>

                </form>

                <p>

                    No account?

                    <Link to="/signup">

                        Signup

                    </Link>

                </p>

            </div>

        </div>

    );

};

export default LoginPage;