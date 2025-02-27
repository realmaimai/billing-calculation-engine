export function EmptyState({ title, description, icon, action }) {
    return (
      <div className="text-center py-12 px-4">
        {icon && <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-gray-100">{icon}</div>}
        <h3 className="mt-2 text-lg font-medium text-gray-900">{title}</h3>
        {description && <p className="mt-1 text-sm text-gray-500">{description}</p>}
        {action && <div className="mt-6">{action}</div>}
      </div>
    );
  }