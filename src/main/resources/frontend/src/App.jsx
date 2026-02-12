import "bootstrap/dist/css/bootstrap.min.css";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import {
  Route,
  RouterProvider,
  createBrowserRouter,
  createRoutesFromElements,
} from "react-router-dom";
import RootLayout from "./components/layout/RootLayout";
import Home from "./components/home/Home";
import Chat from "./components/chat/Chat";
import AdminDashboard from "./components/admin/AdminDashboard";
import Ticket from "./components/ticket/Ticket";

function App() {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path='/' element={<RootLayout />}>
        <Route index element={<Home />} />
        <Route path='/chat' element={<Chat />} />
        <Route path='/tickets' element={<AdminDashboard />} />
        <Route path='/tickets/:ticketId/ticket' element={<Ticket />} />
      </Route>
    )
  );

  return <RouterProvider router={router} />;
}

export default App;
