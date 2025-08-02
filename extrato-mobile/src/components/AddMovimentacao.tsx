import { Ionicons } from "@expo/vector-icons";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { KeyboardAvoidingView, Text, TextInput, TouchableOpacity, View } from "react-native";
import DatePicker from 'react-native-date-picker'
import Dropdown from 'react-native-input-select';

import z from "zod";

interface AddMovimentacaoProps {
    onClose?: () => void;
}

const AddMovimentacaoSchema = z.object({
    informacoesAdicionais: z.string().min(1, "Descrição é obrigatória"),
    valor: z.number().min(0.01, "Valor deve ser maior que zero"),
    data: z.string().min(1, "Data é obrigatória"),
    tipo: z.enum(["entrada", "saida"], "Tipo é obrigatório"),
    idContaBancaria: z.number().min(1, "Conta bancária é obrigatória"),
})

export default function AddMovimentacao({ onClose }: AddMovimentacaoProps) {
    const [open, setOpen] = useState(false)
    const { register, setValue, getValues, handleSubmit, formState: { errors } } = useForm({
        resolver: zodResolver(AddMovimentacaoSchema)
    })

    return (
        <View style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)', flex: 1, justifyContent: "flex-end", }}>
            <View style={{ flex: 1, backgroundColor: '#fff', padding: 20, maxHeight: '70%' }}>
                <View style={{ justifyContent: 'space-between', flexDirection: 'row' }}>
                    <Text style={{ fontSize: 18, fontWeight: 'bold', marginBottom: 20 }}>Adicionar Movimentação</Text>
                    <TouchableOpacity onPress={onClose}>
                        <Ionicons name="close" size={24} color="#000" onPress={onClose} style={{ alignSelf: 'flex-end' }} />
                    </TouchableOpacity>
                </View>

                <KeyboardAvoidingView behavior="padding" style={{ flex: 1 }}>
                    <TextInput
                        placeholder="Valor"
                        style={{ borderBottomWidth: 1, borderColor: '#ccc', marginBottom: 20 }}
                        keyboardType="numeric"
                        onChangeText={text => setValue("valor", parseFloat(text))}
                    />
                    {errors.valor && <Text style={{ color: 'red' }}>{errors.valor.message}</Text>}

                    <DatePicker
                        modal
                        open={open}
                        date={getValues("data") ? new Date(getValues("data")) : new Date()}
                        onConfirm={(date) => {
                            setOpen(false)
                            setValue("data", date.toISOString())
                        }}
                        onCancel={() => {
                            setOpen(false)
                        }}
                    />

                    <TouchableOpacity onPress={() => setOpen(true)} style={{ marginBottom: 20, borderBottomWidth: 1, borderColor: '#ccc', paddingVertical: 10 }}>
                        <Text>{getValues("data") ? `Data: ${new Date(getValues("data")).toLocaleDateString()}` : "Selecione a data"}</Text>
                    </TouchableOpacity>

                    <Dropdown
                        label="Tipo de Movimentação"
                        placeholder="Selecione o tipo de movimentação"
                        options={[
                            { label: "Entrada", value: "entrada" },
                            { label: "Saída", value: "saida" }
                        ]}
                        selectedValue={getValues("tipo")}
                        onValueChange={(value) => setValue("tipo", value as "entrada" | "saida")}
                        primaryColor={'green'}
                        dropdownStyle={{
                            backgroundColor: '#fff',
                            borderColor: errors.tipo ? 'red' : '#ccc',
                        }}
                        labelStyle={{ color: '#000', fontSize: 12, fontWeight: 'bold' }}
                    />

                    <TextInput
                        placeholder="Informações Adicionais"
                        style={{ borderBottomWidth: 1, borderColor: '#ccc', marginBottom: 20 }}
                        multiline={true}
                        numberOfLines={3}
                        onChangeText={text => setValue("informacoesAdicionais", text)}
                    />
                    {errors.informacoesAdicionais && <Text style={{ color: 'red' }}>{errors.informacoesAdicionais.message}</Text>}
                </KeyboardAvoidingView>
            </View>
        </View>
    )
}