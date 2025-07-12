import { Redirect, Stack } from "expo-router";
import { AuthProvider } from "../context/AuthContext";
import {
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query'
import { StatusBar } from "react-native";

const queryClient = new QueryClient()


export default function RootLayout() {
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <StatusBar barStyle="dark-content" backgroundColor="#fff" />
        <Stack>
          <Stack.Screen name="index" options={{ headerShown: false }} />
          <Stack.Screen name="(private)" options={{ headerShown: false }} />
          <Stack.Screen name="(auth)" options={{ headerShown: false }} />
        </Stack>
      </QueryClientProvider>
    </AuthProvider>
  );
}
