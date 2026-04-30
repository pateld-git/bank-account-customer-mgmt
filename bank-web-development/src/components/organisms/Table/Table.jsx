import React from "react";
import "./Table.css";

const Table = ({ columns, data }) => {
  return (
    <div className="table-responsive-wrapper">
      <table className="custom-dashboard-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key} className={`column-header-${col.key}`}>
                {col.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr key={row.id || rowIndex}>
              {columns.map((col) => (
                <td key={col.key} data-label={col.label}>
                  {col.render ? col.render(row) : row[col.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Table;
