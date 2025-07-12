import { Redirect, Stack, useRouter } from "expo-router";
import { useAuth } from "../../context/AuthContext";
import { View, ActivityIndicator, TouchableOpacity } from "react-native";
import { SafeAreaProvider } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";

export default function PrivateLayout() {
  const { isSignedIn, isLoading } = useAuth();
  const router = useRouter();

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
      <Stack.Screen name="(contasBancarias)" options={{
        title: "Contas Bancárias",
        headerShown: true,
        headerRight: () => {
          return <TouchableOpacity onPress={() => router.push("/(private)/(contasBancarias)/cadastro")} style={{ padding: 10, borderRadius: 50, backgroundColor: "#f0f0f0" }}>
            <Ionicons name="add-outline" size={24} color="#1E2A38" />
          </TouchableOpacity>
        }
      }} />
    </Stack>
  </SafeAreaProvider>
} 