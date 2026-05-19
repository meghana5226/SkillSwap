import {
  createContext,
  useState
} from "react";

export const AuthContext =
createContext();

export const AuthProvider =
({ children }) => {

  const getStoredUser=()=>{

    try{

      const user=

      localStorage.getItem(
        "user"
      );

      if(
        !user ||
        user==="undefined"
      ){

        return null;

      }

      return JSON.parse(
        user
      );

    }

    catch(error){

      console.log(
        "Invalid localStorage data"
      );

      localStorage.removeItem(
        "user"
      );

      return null;

    }

  };


  const [user,setUser]=
  useState(
    getStoredUser()
  );


  const login=(

    token,
    userData

  )=>{

    localStorage.setItem(
      "token",
      token
    );

    localStorage.setItem(

      "user",

      JSON.stringify(
        userData
      )

    );

    setUser(
      userData
    );

  };


  const logout=()=>{

    localStorage.removeItem(
      "token"
    );

    localStorage.removeItem(
      "user"
    );

    setUser(null);

  };


  return(

    <AuthContext.Provider

    value={{

      user,
      login,
      logout

    }}

    >

      {children}

    </AuthContext.Provider>

  );

};