const Input = ({
  label,
  error,
  ...props
}) => {

  return (

    <div className="input-group">

      <label>

        {label}

      </label>

      <input

      className="input"

      {...props}

      />

      {

        error &&

        <small>

          {error}

        </small>

      }

    </div>

  );

};

export default Input;