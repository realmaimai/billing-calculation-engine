export function Card({ children, className = '' }) {
    return (
      <div className={`bg-white rounded-lg shadow overflow-hidden ${className}`}>
        {children}
      </div>
    );
  }
  
  export function CardHeader({ title, description, actions }) {
    return (
      <div className="px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-lg font-medium text-gray-900">{title}</h3>
            {description && <p className="text-sm text-gray-500 mt-1">{description}</p>}
          </div>
          {actions && <div>{actions}</div>}
        </div>
      </div>
    );
  }
  
  export function CardBody({ children, className = '' }) {
    return (
      <div className={`px-6 py-4 ${className}`}>
        {children}
      </div>
    );
  }