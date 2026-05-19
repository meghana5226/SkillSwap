const Button = ({
  children,
  loading = false,
  type = "button",
  onClick,
  variant = "primary"
}) => {

  return (

    <button
      type={type}
      onClick={onClick}
      disabled={loading}
      className={`btn ${variant}`}
    >

      {

        loading

        ?

        "Loading..."

        :

        children

      }

    </button>

  );

};

export default Button;