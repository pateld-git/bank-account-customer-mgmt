const Currency = ({ value }) => {
  const formatted = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(value);

  return <span className="currency-text">{formatted}</span>;
};

export default Currency;
