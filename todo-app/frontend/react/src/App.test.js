import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Add task', () => {
  render(<App />);
  const linkElement = screen.getByText(/Add task/i);
  expect(linkElement).toBeInTheDocument();
});
