import { KeyboardAvoidingView, Platform, Text, TextInput, TouchableOpacity, View } from "react-native";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import Dropdown from 'react-native-input-select';
import { useCadastroContaBancaria } from "@/src/hooks/useCadastroContaBancaria";
import { useListagemInstituicaoFinanceira } from "@/src/hooks/useListagemInstituicaoFinanceira";
import { useState } from "react";

const ContaBancariaSchema = z.object({
    numero: z.number().min(1, { error: "Número é obrigatório" }),
    agencia: z.number().min(1, { error: "Agência é obrigatória" }),
    instituicaoFinanceiraId: z.number().min(1, { error: "Instituição Financeira é obrigatória" }),
    tipo: z.enum(["CORRENTE", "POUPANCA", "SALARIO"])
});

export type ContaBancariaFormData = z.infer<typeof ContaBancariaSchema>;

export default function CadastroContaBancaria() {
    const { mutate } = useCadastroContaBancaria();
    const [filtroInstituicao, setFiltroInstituicao] = useState<string | undefined>('');
    const { data: instituicoesFinanceiras, refetch } = useListagemInstituicaoFinanceira(10, filtroInstituicao);

    const { control, handleSubmit, formState: { errors } } = useForm({
        resolver: zodResolver(ContaBancariaSchema),
        mode: "all",
    })

    return (
        <KeyboardAvoidingView style={{ flex: 1, padding: 20, backgroundColor: '#fff' }} behavior={Platform.OS === "ios" ? "padding" : "height"}>
            <View style={{ marginBottom: 20, boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)", padding: 20, borderRadius: 8 }}>
                <Text style={{ fontSize: 18, fontWeight: "bold" }}>Cadastro de Conta Bancária</Text>
                <Text>Utilize o formulário abaixo para cadastrar uma nova conta bancária.</Text>
            </View>

            <View style={{ marginBottom: 20, boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)", padding: 20, borderRadius: 8 }}>
                <Controller
                    control={control}
                    name="numero"
                    render={({ field: { onChange, onBlur, value } }) => (
                        <>
                            <Text style={{ fontSize: 12, marginBottom: 5, fontWeight: "bold" }}>Número da Conta</Text>
                            <TextInput
                                placeholder="Número da Conta"
                                onChangeText={(text) => onChange(Number(text))}
                                onBlur={onBlur}
                                value={value !== undefined && value !== null ? String(value) : ""}
                                keyboardType="numeric"
                                style={{
                                    borderWidth: 1,
                                    borderColor: errors.numero ? 'red' : '#ccc',
                                    borderRadius: 4,
                                    padding: 10,
                                    marginTop: 5
                                }}
                            />
                            {errors.numero && <Text style={{ color: 'red' }}>{errors.numero.message}</Text>}
                        </>
                    )}
                />

                <Controller
                    control={control}
                    name="agencia"
                    render={({ field: { onChange, onBlur, value } }) => (
                        <View style={{ marginTop: 20 }}>
                            <Text style={{ fontSize: 12, marginBottom: 5, fontWeight: "bold" }}>Agência</Text>
                            <TextInput
                                placeholder="Agência"
                                onChangeText={(text) => onChange(Number(text))}
                                onBlur={onBlur}
                                value={value !== undefined && value !== null ? String(value) : ""}
                                keyboardType="numeric"
                                style={{
                                    borderWidth: 1,
                                    borderColor: errors.agencia ? 'red' : '#ccc',
                                    borderRadius: 4,
                                    padding: 10,
                                    marginTop: 5
                                }}
                            />
                            {errors.agencia && <Text style={{ color: 'red' }}>{errors.agencia.message}</Text>}
                        </View>
                    )}
                />

                <Controller
                    control={control}
                    name="instituicaoFinanceiraId"
                    render={({ field: { onChange, onBlur, value } }) => (
                        <View style={{ marginTop: 20 }}>
                            <Dropdown
                                label="Instituição Financeira"
                                placeholder="Selecione a instituição financeira"
                                options={instituicoesFinanceiras?.pages.flatMap((page) => page.dados).map((instituicao) => ({
                                    label: instituicao.nome,
                                    value: instituicao.id,
                                })) || []}
                                selectedValue={value}
                                isSearchable
                                searchControls={{
                                    textInputProps: { placeholder: 'Buscar instituição' },
                                    searchCallback: (text) => {                                        
                                        setFiltroInstituicao(text);
                                        refetch();
                                    }
                                }}
                                onValueChange={onChange}
                                primaryColor={'green'}
                                dropdownStyle={{
                                    backgroundColor: '#fff',
                                    borderColor: errors.instituicaoFinanceiraId ? 'red' : '#ccc',
                                }}
                                labelStyle={{ color: '#000', fontSize: 12, fontWeight: 'bold' }}
                            />
                            {errors.instituicaoFinanceiraId && <Text style={{ color: 'red' }}>{errors.instituicaoFinanceiraId.message}</Text>}
                        </View>
                    )}
                />
                <Controller
                    control={control}
                    name="tipo"
                    render={({ field: { onChange, onBlur, value } }) => (
                        <View style={{ marginTop: 20 }}>
                            <Dropdown
                                label="Tipo de Conta"
                                placeholder="Selecione o tipo de conta"
                                options={[
                                    { label: 'Corrente', value: 'CORRENTE' },
                                    { label: 'Poupança', value: 'POUPANCA' },
                                    { label: 'Salário', value: 'SALARIO' },
                                ]}
                                selectedValue={value}
                                onValueChange={onChange}
                                primaryColor={'green'}
                                dropdownStyle={{
                                    backgroundColor: '#fff',
                                    borderColor: errors.tipo ? 'red' : '#ccc',
                                }}
                                labelStyle={{ color: '#000', fontSize: 12, fontWeight: 'bold' }}
                            />
                            {errors.tipo && <Text style={{ color: 'red' }}>{errors.tipo.message}</Text>}
                        </View>
                    )}
                />

                <TouchableOpacity onPress={handleSubmit((data) => mutate(data))} style={{ marginTop: 20, backgroundColor: 'transparent', borderColor: 'green', borderWidth: 1, padding: 15, borderRadius: 4, alignItems: 'center' }}>
                    <Text style={{ color: 'green', fontWeight: 'bold' }}>Cadastrar Conta</Text>
                </TouchableOpacity>
            </View>
        </KeyboardAvoidingView>
    );
}