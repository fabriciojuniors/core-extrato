import { Redirect, Stack } from "expo-router";
import { useAuth } from "../../context/AuthContext";
import { View, ActivityIndicator } from "react-native";
import { SafeAreaProvider } from "react-native-safe-area-context";

export default function PrivateLayout() {
  const { isSignedIn, isLoading } = useAuth();

  if (isLoading) {
    return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (!isSignedIn) {
    return <Redirect href="/(auth)/login" />;
  }

  return <SafeAreaProvider>
    <Stack>
      <Stack.Screen name="index" options={{ headerShown: false, title: "Início" }} />
      <Stack.Screen name="contasBancarias" options={{
        title: "Contas Bancárias",
        headerShown: true,
      }} />
    </Stack>
  </SafeAreaProvider>
} 