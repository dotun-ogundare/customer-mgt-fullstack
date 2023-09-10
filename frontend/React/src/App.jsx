import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react';
import SidebarWithHeader from "./shared/SideBar";
import { useEffect, useState } from 'react';
import {getCustomers} from "./components/services/client.js";
import SocialProfileWithImage from "./components/Card";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            getCustomers().then(res => {
                setCustomers(res.data)
            }).catch(err => {
                console.log(err)
            }).finally( () => {
                setLoading(false);
            })
        }, 500)
    }, [])

    if(loading){
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
            )
    }

    if(customers.length <= 0){
        return (
            <SidebarWithHeader>
                <Text> No Customers Available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Wrap justify ={"center"} spacing ={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <SocialProfileWithImage
                            {...customer}

                        />
                    </WrapItem>
                ))}
            </Wrap>

        </SidebarWithHeader>

    )
}

export default App;