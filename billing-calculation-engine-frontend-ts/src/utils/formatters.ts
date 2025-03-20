/**
 * Formats a number as currency with dollar sign and 2 decimal places
 */
export const formatCurrency = (amount?: number | null): string => {
  if (amount === undefined || amount === null) return '$0.00';
  return `$${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
};

export const formatCurrencyWithSymbol = (amount:number, currency:string) => {
  const currencySymbol = currency === 'CAD' ? 'CA$' : 'US$';
  return `${currencySymbol}${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
};

/**
 * Formats a date string to a readable format
 */
export const formatDate = (dateString?: string | null): string => {
  if (!dateString) return 'N/A';

  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString('en-US', options);
};